import './App.css';
import { useState, useRef, useEffect } from 'react';
import Calendar from './Calendar';
import Operation from './ Operation';

function App() {
  const today = new Date();
  const currentMonth = today.getMonth();

  //useStateの定義
  const [month, setMonth] = useState(currentMonth);
  const [year, setYear] = useState(0);
  const [newTaskModal, setNewTaskModal] = useState('none');
  const [tasksModal, setTasksModal] = useState('none');
  const [alertModal, setAlertModal] = useState('none');
  const [loginModal, setLoginModal] = useState('none');
  const [selectedDay, setSelectedDay] = useState();
  const [DayryTaskList, setDayryTaskList] = useState();
  const [userName, setUserName] = useState();
  const [userId, setUserId] = useState();
  const [alert, setAlert] = useState();
  const [loginButton, setLoginButton] = useState('Log in');
  const [userTasks, setUserTasks] = useState([]);

  //useRefの定義
  const refNewTask = useRef();
  const refYear = useRef();
  const refMonth = useRef();
  const refDate = useRef();
  const refHour = useRef();
  const refMinute = useRef();
  const refUserName = useRef();
  const refPassword = useRef();

  //Appがマウントされた時にログイン画面のモーダルを表示
  useEffect(() => {
    setLoginModal('block');
  }, []);

  //年、月、日、時、分の選択肢生成
  const optionOfYear = [];
  for (let i = 2000; i <= 2100; i++) {
    if (i === 2025) {
      optionOfYear.push(
        <option key={i} value={i} selecred>
          {i}
        </option>
      );
    } else {
      optionOfYear.push(
        <option key={i} value={i}>
          {i}
        </option>
      );
    }
  }
  const optionOfMonth = [];
  for (let i = 0; i <= 11; i++) {
    if (i === today.getMonth()) {
      optionOfMonth.push(
        <option key={i} value={i + 1} selected>
          {i + 1}
        </option>
      );
    } else {
      optionOfMonth.push(
        <option key={i} value={i + 1}>
          {i + 1}
        </option>
      );
    }
  }
  const optionOfDay = [];
  for (
    let i = 1;
    i <= new Date(today.getFullYear(), today.getMonth() + 1, 0).getDate();
    i++
  ) {
    if (i === today.getDate()) {
      optionOfDay.push(
        <option key={i} value={i} selected>
          {i}
        </option>
      );
    } else {
      optionOfDay.push(
        <option key={i} value={i}>
          {i}
        </option>
      );
    }
  }
  const optionOfHour = [];
  for (let i = 0; i <= 24; i++) {
    if (i === today.getHours()) {
      optionOfHour.push(
        <option kye={i} value={i} selected>
          {i}
        </option>
      );
    } else {
      optionOfHour.push(
        <option key={i} value={i}>
          {i}
        </option>
      );
    }
  }
  const optionOfMinute = [];
  for (let i = 0; i <= 45; i += 15) {
    if (i === Math.floor(today.getMinutes() / 15) * 15) {
      optionOfMinute.push(
        <option keu={i} value={i} selected>
          {i}
        </option>
      );
    } else {
      optionOfMinute.push(
        <option key={i} value={i}>
          {i}
        </option>
      );
    }
  }

  //ログインしているユーザーのタスクリストの生成
  async function createTaskList(id) {
    console.log(id);
    const res = await fetch(`/tasks/user_id/${id}`, {
      method: 'GET',
    });
    const tasks = await res.json();
    await setUserTasks(tasks);
    console.log(tasks);
  }

  //新しいタスクの追加
  async function addNewTask() {
    if (!userId || !refNewTask.current.value) {
      setAlert('user or task is not defined');
      setNewTaskModal('none');
      setAlertModal('block');
      return;
    }
    const taskObj = {
      userId: userId,
      year: refYear.current.value,
      month: refMonth.current.value,
      date: refDate.current.value,
      hour: refHour.current.value,
      minute: refMinute.current.value,
      task: refNewTask.current.value,
    };
    console.log(taskObj);
    await fetch(`/tasks`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(taskObj),
    });
    await createTaskList(userId)
    clearRefInput();
    setNewTaskModal('none');
  }



  //パスワードのハッシュを比較してログイン
  async function login() {
    try {
      const res = await fetch('/users/get', {
        method: 'GET',
        headers: {"Content-Type": "application/json"},
        });
      const allUsers = await res.json();
      console.log(allUsers);
    } catch(err) {
      console.error("Error fetching users:", err);
    }

    const userName = refUserName.current.value;
    const password = refPassword.current.value;

    console.log(JSON.stringify({ userName: userName, password: password }))

    try{
      const res =await fetch("/users/login",{
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ userName: userName, password: password }),
      });
      const state = await res.text();
      console.log(state);
        if (state === 'success') {
          setUserName(userName);
          const res = await fetch(`users/get/name/${userName}`, {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            },
          });
          const id = await res.text()
          console.log(id);
          await setUserId(id)
          setLoginModal('none');
          setLoginButton('Log out');
          console.log(userTasks);
          await createTaskList(id);
        } else {
          setLoginButton('Log in');
          setAlert('incorrect user name of password');
          setAlertModal('block');
        }
    } catch (err) {
      console.log("error")
    }

    clearRefInput();
    setLoginModal('none');
  }

  function clearRefInput() {
    refNewTask.current.value = '';
    refUserName.current.value = '';
    refPassword.current.value = '';
  }

  return (
    <>
      {/* タイトルの描画 */}
      <span style={{ fontSize: 40, marginLeft: 350 }}>Simple scheduler</span>
      <span style={{ marginLeft: 250, marginRight: 0, fontSize: 20 }}>
        user:{userName}
      </span>

      {/* 操作ボタンの描画 */}
      <Operation
        month={month}
        setMonth={setMonth}
        setYear={setYear}
        setNewTaskModal={setNewTaskModal}
        userTasks={userTasks}
        setLoginModal={setLoginModal}
        loginButton={loginButton}
        setLoginButton={setLoginButton}
        setUserName={setUserName}
        setUserId={setUserId}
        setTaskList={setDayryTaskList}
        setUserTasks={setUserTasks}
      />
      {/* カレンダーの描画 */}
      <Calendar
        month={month}
        setMonth={setMonth}
        year={year}
        setYear={setYear}
        setTasksModal={setTasksModal}
        setSelectedDay={setSelectedDay}
        setDayryTaskList={setDayryTaskList}
        userTasks={userTasks}
      />

      {/* タスク追加のモーダル */}
      <div style={{ display: newTaskModal }} className="modal">
        <div className="modal-content">
          <div className="closeModal" onClick={() => setNewTaskModal('none')}>
            &times;
          </div>
          <p width={300}>Add new task</p>
          <span>
            <select name="year" className="input" ref={refYear} defaultValue={today.getFullYear()}>
              {optionOfYear}
            </select>
            年
            <select name="month" className="input" ref={refMonth}>
              {optionOfMonth}
            </select>
            月
            <select name="date" className="input" ref={refDate}>
              {optionOfDay}
            </select>
            日 &emsp;
            <select name="hour" className="input" ref={refHour}>
              {optionOfHour}
            </select>
            時
            <select name="minute" className="input" ref={refMinute}>
              {optionOfMinute}
            </select>
            分
          </span>
          <div style={{ margin: 20 }}>
            <input
              style={{ width: 300, borderRadius: 10 }}
              placeholder="Please, enter your new task"
              className="input"
              ref={refNewTask}
            ></input>
          </div>
          <button onClick={addNewTask}>Add to task list</button>
          <div></div>
        </div>
      </div>

      {/* デイリータスク表示のモーダル */}
      <div style={{ display: tasksModal }} className="modal">
        <div className="modal-content">
          <span className="closeModal" onClick={() => setTasksModal('none')}>
            &times;
          </span>
          <h3>
            <u>{selectedDay}</u>
          </h3>
          <div style={{ marginLeft: 30 }}>
            <list style={{ textAlign: 'left' }}>{DayryTaskList}</list>
          </div>
        </div>
      </div>

      {/* 警告のモーダル */}
      <div style={{ display: alertModal }} className="modal">
        <div className="modal-content">
          <span className="closeModal" onClick={() => setAlertModal('none')}>
            &times;
          </span>
          <h3>{alert}</h3>
        </div>
      </div>

      {/* ログイン画面のモーダル */}
      <div style={{ display: loginModal }} className="modal">
        <div className="modal-content" style={{ width: 300 }}>
          <span
            className="closeModal"
            onClick={() => {
              setLoginModal('none');
              setLoginButton('Log in');
            }}
          >
            &times;
          </span>
          <h3>Login/Signup</h3>
          <div style={{ textAlign: 'left', marginLeft: 80, marginBottom: 30 }}>
            <h4 style={{ marginBottom: 0 }}>User name</h4>
            <input ref={refUserName}></input>
            <h4 style={{ marginBottom: 0 }}>Password</h4>
            <input ref={refPassword} type="password"></input>
            <button style={{ marginTop: 30, marginLeft: 30 }} onClick={login}>
              Log in
            </button>
            <button style={{ marginTop: 30, marginLeft: 30 }} onClick={signup}>
              Log in
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

export default App;
