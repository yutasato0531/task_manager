package com.example.task_manager

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskManagerApplicationTasks(
	@Autowired val restTemplate: TestRestTemplate,
	@LocalServerPort val port: Int,
	@Autowired val repository: UsersRepository
) {
	@Test
	fun contextLoads() {
	}

	@BeforeEach
	fun setup() {
		// 各テストは項目が空の状態で始める。
		repository.deleteAll()
	}

	@Test
	fun `GETリクエストはOKステータスを返す`() {
		// localhost/todos に GETリクエストを発行する。
		val res = restTemplate.getForEntity("http://localhost:$port/tasks", String::class.java)
		// レスポンスのステータスコードは OK である。
		assertThat(res.statusCode, equalTo(HttpStatus.OK))
	}

	@Test
	fun `POSTリクエストはOKステータスを返す`() {
		val req = TasksRequest(1, "task", 2025, 6, 11, 18, 30)
		val response = restTemplate.postForEntity("http://localhost:$port/tasks", req, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
	}

	@Test
	fun `GETリクエストはすべてのタスクリストを返す`() {
		// localhost/todos に GETリクエストを送り、レスポンスを TodoEntity の配列として解釈する。
		val res = restTemplate.getForEntity("http://localhost:$port/tasks", Array<TasksEntity>::class.java)
		val tasks = res.body!!

		// 配列は0個の要素をもつこと。
		assertThat(tasks.size, equalTo(0))
	}

	@Test
	fun `POSTリクエストはuserオブジェクトを格納する`() {
		val req = TasksRequest(1, "task", 2025, 6, 11, 18, 30)
		restTemplate.postForEntity("http://localhost:$port/tasks", req, String::class.java)

		val res = restTemplate.getForEntity("http://localhost:$port/tasks", Array<TasksEntity>::class.java)
		val tasks= res.body!!

		assertThat(tasks.size, equalTo(1))
		assertThat(tasks[0].task, equalTo("task"))
	}


	@Test
	fun `特定のuser_idを指定してtask_idとtaskをGETできる`() {
		// 項目を新規作成する。
		val req1 = TasksRequest(1, "task1", 2025, 6, 11, 18, 30)
		restTemplate.postForEntity("http://localhost:$port/tasks", req1, String::class.java)
		val req2 = TasksRequest(2, "task2", 2025, 6, 13, 18, 30)
		restTemplate.postForEntity("http://localhost:$port/tasks", req2, String::class.java)

		// localhost/todos/$id に GETリクエストを送り、レスポンスを1個の TodoEntity として解釈する。
		val res = restTemplate.getForEntity("http://localhost:$port//tasks/task_id/2", TasksEntity::class.java)
		val taskById = res.body!!

		// 新規作成したものと内容が一致している。
		assertThat(taskById.taskId, equalTo(2))
		assertThat(taskById.task, equalTo("task2"))
	}

	@Test
	fun `存在しないIDでGETするとステータス404を返す`() {
		// id=999 を指定して GETリクエストを送る。
		val res = restTemplate.getForEntity("http://localhost:$port//tasks/task_id/999", TasksEntity::class.java)

		// レスポンスのステータスコードは NOT_FOUND である。
		assertThat(res.statusCode, equalTo(HttpStatus.NOT_FOUND))
	}

	@Test
	fun `DELETEでuser_idを指定して削除できる`() {
		// 項目を新規作成する。
		val req1 = TasksRequest(1, "task1", 2025, 6, 11, 18, 30)
		restTemplate.postForEntity("http://localhost:$port/tasks", req1, String::class.java)
		val req2 = TasksRequest(2, "task2", 2025, 6, 13, 18, 30)
		restTemplate.postForEntity("http://localhost:$port/tasks", req2, String::class.java)

		// localhost/todos/$id に DELETEリクエストを送る。
		val id: Int = 2
		restTemplate.delete("http://localhost:$port//tasks/${id}")

		// 再度GETすると、その項目は存在しない (削除されている)。
		val deletedTask = restTemplate.getForEntity("http://localhost:$port//tasks/task_id/${id}", TasksEntity::class.java)
		val res = restTemplate.getForEntity("http://localhost:$port//tasks/task_id/1", TasksEntity::class.java)
		val task = res.body!!

		assertThat(deletedTask.statusCode, equalTo(HttpStatus.NOT_FOUND))
		assertThat(task.task, equalTo("task1"))
	}

}
