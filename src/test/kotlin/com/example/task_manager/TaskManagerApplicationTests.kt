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
class TaskManagerApplicationTests(
	@Autowired val restTemplate: TestRestTemplate,
	@LocalServerPort val port: Int,
	@Autowired val repository: TestRepository
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
		val response = restTemplate.getForEntity("http://localhost:$port/test", String::class.java)
		// レスポンスのステータスコードは OK である。
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
	}

	@Test
	fun `POSTリクエストはOKステータスを返す`() {
		val req = TestRequest("hello")
		val response = restTemplate.postForEntity("http://localhost:$port/test", req, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
	}

	@Test
	fun `GETリクエストは空のTodoリストを返す`() {
		// localhost/todos に GETリクエストを送り、レスポンスを TodoEntity の配列として解釈する。
		val res = restTemplate.getForEntity("http://localhost:$port/test", Array<TestEntity>::class.java)
		val tasks = res.body!!

		// 配列は0個の要素をもつこと。
		assertThat(tasks.size, equalTo(0))
	}

	@Test
	fun `POSTリクエストはTodoオブジェクトを格納する`() {
		val req = TestRequest("hello")
		restTemplate.postForEntity("http://localhost:$port/test", req, String::class.java)

		val res = restTemplate.getForEntity("http://localhost:$port/test", Array<TestEntity>::class.java)
		val tasks = res.body!!

		assertThat(tasks.size, equalTo(1))
		assertThat(tasks[0].text, equalTo("hello"))
	}

	@Test
	fun `POSTリクエストは新規作成した項目のIDを返す`() {
		// 項目を新規作成し、返されたIDを取得する。
		val req = TestRequest("hello")
		restTemplate.postForEntity("http://localhost:$port/test", req, String::class.java)

		// TodoEntity の配列を取得する。
		val res = restTemplate.getForEntity("http://localhost:$port/test", Array<TestEntity>::class.java)
		val tasks = res.body!!

		// 配列中に返されたIDをもつ要素があること。
		assertThat(tasks[0].id, equalTo(1))
	}

	@Test
	fun `特定の項目をIDを指定してGETできる`() {
		// 項目を新規作成する。
		val req1 = TestRequest("hello")
		restTemplate.postForEntity("http://localhost:$port/test", req1, String::class.java)
		val req2 = TestRequest("bye bye")
		restTemplate.postForEntity("http://localhost:$port/test", req2, String::class.java)

		// localhost/todos/$id に GETリクエストを送り、レスポンスを1個の TodoEntity として解釈する。
		val res = restTemplate.getForEntity("http://localhost:$port/test/2", Array<TestEntity>::class.java)
		val taskById = res.body!!

		// 新規作成したものと内容が一致している。
		assertThat(taskById[0].id, equalTo(2))
		assertThat(taskById[0].text, equalTo("bye bye"))
	}

	@Test
	fun `存在しないIDでGETするとステータス404を返す`() {
		// id=999 を指定して GETリクエストを送る。
		val res = restTemplate.getForEntity("http://localhost:$port/test/999", Array<TestEntity>::class.java)

		// レスポンスのステータスコードは NOT_FOUND である。
		assertThat(res.statusCode, equalTo(HttpStatus.NOT_FOUND))
	}

	@Test
	fun `DELETEでIDを指定して削除できる`() {
		// 項目を新規作成する。
		val req1 = TestRequest("hello")
		restTemplate.postForEntity("http://localhost:$port/test", req1, String::class.java)
		val req2 = TestRequest("bye bye")
		restTemplate.postForEntity("http://localhost:$port/test", req2, String::class.java)

		// localhost/todos/$id に DELETEリクエストを送る。
		restTemplate.delete("http://localhost:$port/test/1",String::class.java)

		// 再度GETすると、その項目は存在しない (削除されている)。
		val res = restTemplate.getForEntity("http://localhost:$port/test/1", Array<TestEntity>::class.java)

		assertThat(res.statusCode, equalTo(HttpStatus.NOT_FOUND))
	}

}
