import './App.css';
import './index.css';
// import { useRef } from 'react';

function Operation(props) {
  // const refInput = useRef(null);

  function thisMonth() {
    props.setYear(0);
    props.setMonth(new Date().getMonth());
  }

  function newTaskModalOpen() {
    props.setNewTaskModal('block');
  }

  function loginLogout() {
    console.log(props.loginButton);
    if(props.loginButton === 'Log out'){
      props.setUserName('');
      props.setUserId('');
      props.setTaskList([]);
      props.setUserTasks([]);
      
      props.setLoginButton('Log in')
    }else{
      props.setLoginModal('block')
    }
  }

  return (
    <>
      <div className="buttonContainer">
        <button
          style={{
            marginLeft: 10,
            height: 40,
            width: 100,
            color: 'black',
            backgroundColor: 'white',
          }}
          onClick={loginLogout}
        >
          {props.loginButton}
        </button>
        <button
          style={{ color: 'white', backgroundColor: 'black', marginLeft:330 }}
          onClick={() => thisMonth()}
        >
          This month
        </button>
        <button
          style={{
            marginLeft: 300,
            color: 'white',
            backgroundColor: 'black',
            visibility: props.loginButton,
          }}
          onClick={() => newTaskModalOpen()}
        >
          New task
        </button>
      </div>
    </>
  );
}

export default Operation;
