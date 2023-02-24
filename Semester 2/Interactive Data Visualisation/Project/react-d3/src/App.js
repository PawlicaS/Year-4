import './App.css';
import React from 'react';
import LineGraph from './Components/LineGraph';

const App = () => {
  const data = [
    { date: new Date(2022, 0, 1), value1: 10, value2: 20 },
    { date: new Date(2022, 1, 1), value1: 20, value2: 30 },
    { date: new Date(2022, 2, 1), value1: 15, value2: 25 },
    { date: new Date(2022, 3, 1), value1: 25, value2: 35 },
    { date: new Date(2022, 4, 1), value1: 30, value2: 40 },
    { date: new Date(2022, 5, 1), value1: 20, value2: 30 }
  ];
  return (
    <div className="container">
      <LineGraph data={data} />
    </div>
  );
};

export default App;