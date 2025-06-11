import './App.css';
import './index.css';
// import { useState, useRef, useEffect } from 'react';

function Calendar(props) {


  const date = new Date();

  const monthName = {
    0: 'January',
    1: 'Feburary',
    2: 'March',
    3: 'April',
    4: 'May',
    5: 'June',
    6: 'July',
    7: 'August',
    8: 'September',
    9: 'October',
    10: 'November',
    11: 'December',
  };

  const monthDays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

  //曜日行の生成
  const dayOfWeek = [];
  for (let i = 0; i < 7; i++) {
    if (i === 0) {
      dayOfWeek.push(<th className="sun">{monthDays[i]}</th>);
    } else if (i === 6) {
      dayOfWeek.push(<th className="sat">{monthDays[i]}</th>);
    } else {
      dayOfWeek.push(<th className="weekDay">{monthDays[i]}</th>);
    }
  }

  //日付の生成
  const weeks = [];
  function createDays(month, year) {
    console.log('in カレンダー', props.userTasks)
    //現在の西暦を取得
    const currentYear = date.getFullYear() + year;

    //翌月の0日目から当月の最終日を設定 .getDate()はDateインスタンスの日を取得
    const daysInMonth = new Date(currentYear, month + 1, 0).getDate();

    //当月の初日の曜日を取得 .getDay()はDateインスタンスの曜日取得
    const firstDay = new Date(currentYear, month, 1).getDay();

    //当月の0日目から前月の最終日を取得
    const daysInPrevMonth = new Date(currentYear, month, 0).getDate();

    let dayCount = 1;

    //前月最終日から当月初日の曜日を引いて１を足す(曜日は0から始まるインデックス)ことでカレンダーに表示する前月の日付の先頭の日付を取得
    let prevDayCount = daysInPrevMonth - firstDay + 1;

    //１週間の7日分を来月の日付を７日以上生成するまで繰り返す
    for (let i = 0; i < 6; i++) {
      const days = [];
      for (let j = 0; j < 7; j++) {
        if (i === 0 && j < firstDay) {
          console.log();
          days.push(
            <td
              className="mute"
              onClick={() =>
                month === 0
                  ? viewTasks(currentYear - 1, 12, prevDayCount)
                  : viewTasks(currentYear, month, prevDayCount)
              }
            >
              <a className="muteA" href="#">
                {prevDayCount}
                <br />
                  {month === 0
                    ? countDayryTask(currentYear - 1, 12, prevDayCount)
                    : countDayryTask(currentYear - 1, month, prevDayCount)}
              </a>
            </td>
          );
          prevDayCount++;
        } else if (dayCount > daysInMonth) {
          let nextMonthDayCount = dayCount - daysInMonth;
          days.push(
            <td
              className="mute"
              onClick={() =>
                month === 11
                  ? viewTasks(currentYear + 1, 1, nextMonthDayCount)
                  : viewTasks(currentYear, month + 2, nextMonthDayCount)
              }
            >
              <a className="muteA" href="#">
                {nextMonthDayCount}
                <br />
                  {month === 11
                    ? countDayryTask(currentYear + 1, 1, nextMonthDayCount)
                    : countDayryTask(currentYear + 1, month, nextMonthDayCount)}
              </a>
            </td>
          );
          dayCount++;
        } else {
          const day = dayCount;
          if (
            dayCount === date.getDate() &&
            year === 0 &&
            month === date.getMonth()
          ) {
            days.push(
              <td
                className="today"
                onClick={() => viewTasks(currentYear, month + 1, day)}
              >
                <a className="todayA" href="#">
                  {dayCount}
                  <br />
                  {countDayryTask(currentYear, month + 1, day)}
                </a>
              </td>
            );
            dayCount++;
          } else {
            days.push(
              <td
                className="days"
                onClick={() => viewTasks(currentYear, month + 1, day)}
              >
                <a className="daysA" href="#">
                  {dayCount}
                  <br />
                  {countDayryTask(currentYear, month + 1, day)}
                </a>
              </td>
            );
            dayCount++;
          }
        }
      }
      weeks.push(<tr>{days}</tr>);

      if (dayCount - daysInMonth > 7) {
        // break;
      }
    }
  }

  createDays(props.month, props.year);

  //デイリータスクの件数カウント
  function countDayryTask(year, month, date) {
    const countTaskas = props.userTasks.filter(
      (task) => task.year == year && task.month == month && task.date == date
    ).length;
    if(countTaskas === 0) {
      return <span style={{fontSize: 15, color: 'gray'}}>no task</span>;
    }else if(countTaskas === 1){
      return <u style={{fontSize: 15}}>{countTaskas} task</u>;
    }else{
      return <u style={{fontSize: 15}}>{countTaskas} tasks</u>;
    }
  }

  //デイリータクスク要素の生成
  function viewTasks(year, month, date) {
    const filteredTasks = props.userTasks.filter(
      (task) => task.year === year && task.month === month && task.date === date
    );
    const htmlTasks = filteredTasks.map((item, index) => (
      <li key={index}>
        {item.hour}時{item.minute}分：{item.task}
      </li>
    ));
    console.log(props.userTasks);
    props.setDayryTaskList(htmlTasks);
    props.setTasksModal('block');
    props.setSelectedDay(`${year}年 ${month}月${date}日`);
  }

  //カレンダーの月を移動
  function monthMove(direction) {
    if (direction && props.month === 11) {
      createDays(0, props.year + 1);
      props.setYear(props.year + 1);
      props.setMonth(0);
    } else if (!direction && props.month === 0) {
      createDays(11, props.year - 1);
      props.setYear(props.year - 1);
      props.setMonth(11);
    } else if (direction) {
      createDays(props.month + 1, props.year);
      props.setMonth(props.month + 1);
    } else if (!direction) {
      createDays(props.month - 1, props.year);
      props.setMonth(props.month - 1);
    }
  }

  return (
    <>
      <h2>
        <table align="center">
          <tr>
            <th className="moveMonth" onClick={() => monthMove(false)}>
              <a href="#">&lt;</a>
            </th>
            <th className="yearAndMonth">
              &emsp;
              {monthName[props.month]}.{date.getFullYear() + props.year}
              &emsp;
            </th>
            <th className="moveMonth" onClick={() => monthMove(true)}>
              <a href="#">&gt;</a>
            </th>
          </tr>
        </table>
      </h2>
      <div id="calendar" className="calendar-wrap">
        <table className="calendar">
          <tr>{dayOfWeek}</tr>
          {weeks}
        </table>
      </div>
    </>
  );
}

export default Calendar;
