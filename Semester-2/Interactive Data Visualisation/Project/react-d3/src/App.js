import './App.css';
import * as d3 from 'd3';
import React from 'react';
import LineGraph from './Components/LineGraph';

const App = () => {
  const [data, setData] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  React.useEffect(() => {
    d3.json("/values.json").then((d) => {
      setData(d);
      setLoading(false);
    });
    return () => undefined;
  }, []);
  return (
    <div className="App">
      {loading && <div>loading</div>}
      {!loading && <LineGraph data={data} />}
    </div>
  );
};

export default App;