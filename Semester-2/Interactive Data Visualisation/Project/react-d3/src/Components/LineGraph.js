import React, { useRef, useEffect, useState } from 'react';
import * as d3 from 'd3';

const LineGraph = ({data}) => {
  const svgRef = useRef();
  const xScaleRef = useRef();
  const yScaleRef = useRef();
  const xAxisRef = useRef();
  const yAxisRef = useRef();
  const lineRef = useRef();
  const zoomRef = useRef();
  const [mousePosition, setMousePosition] = useState(null);
  

  useEffect(() => {
    d3.select("svg").remove();
    var retailData = [];
    var keyshopsData = [];
    console.log(data);
    for (var i = 0; i < data.chartData.retail.length; i++) {
      if (data.chartData.retail[i].y != null) {
        if (data.chartData.retail[i].x != null) {
          var xVal = new Date(data.chartData.retail[i].x);
          retailData.push({date: xVal, price: data.chartData.retail[i].y})
        }
      }
    }
    for (var i = 0; i < data.chartData.keyshops.length; i++) {
      if (data.chartData.keyshops[i].y != null) {
        if (data.chartData.keyshops[i].x != null) {
          var xVal = new Date(data.chartData.keyshops[i].x);
          keyshopsData.push({date: xVal, price: data.chartData.keyshops[i].y})
        }
      }
    }
    console.log(retailData);
    console.log(keyshopsData);
    const margin = { top: 20, right: 20, bottom: 30, left: 30 };
    const width = 1200 - margin.left - margin.right;
    const height = 600 - margin.top - margin.bottom;

    const xScale = d3.scaleTime()
      .range([0, width])
      .domain(d3.extent([...retailData, ...keyshopsData], d => d.date));
    xScaleRef.current = xScale;

    const yScale = d3.scaleLinear()
      .range([height, 0])
      .domain([0, d3.max([...retailData, ...keyshopsData], d => d.price)]);
    yScaleRef.current = yScale;
      
    const line = d3.line()
      .x(d => xScale(d.date))
      .y(d => yScale(d.price));
    lineRef.current = line;

    const svg = d3.select(svgRef.current)
      .append('svg')
        .attr('width', width + margin.left + margin.right)
        .attr('height', height + margin.top + margin.bottom)
      .append('g')
        .attr('transform', `translate(${margin.left}, ${margin.top})`);

    
    const xAxis = d3.axisBottom(xScale);
    xAxisRef.current = xAxis;
    svg.append('g')
        .attr('transform', `translate(0, ${height})`)
        .call(xAxis);

    const yAxis = d3.axisLeft(yScale);
    yAxisRef.current = yAxis;
    svg.append('g')
        .call(yAxis);

    svg.append('path')
        .datum(retailData)
        .attr('fill', 'none')
        .attr('stroke', 'blue')
        .attr('stroke-width', 1.5)
        .attr('d', line);

    svg.append('path')
        .datum(keyshopsData)
        .attr('fill', 'none')
        .attr('stroke', 'orange')
        .attr('stroke-width', 1.5)
        .attr('d', line);

    const zoomBehavior = d3.zoom().on('zoom', () => {
      const newXScale = d3.event.transform.rescaleX(xScaleRef.current);
      const newYScale = d3.event.transform.rescaleY(yScaleRef.current);
      xAxisRef.current.call(d3.axisBottom(newXScale));
      yAxisRef.current.call(d3.axisLeft(newYScale));
      lineRef.current.x((d) => newXScale(d.date)).y((d) => newYScale(d.value));
      svg.select('path').attr('d', lineRef.current);
    });
    zoomRef.current = zoomBehavior;

    svg.call(zoomBehavior);

    svg.on('mousemove', () => {
      const [x, y] = d3.mouse(svgRef.current);
      const date = xScale.invert(x);
      const bisect = d3.bisector((d) => d.date).left;
      const index = bisect(data, date);
      const d0 = data[index - 1];
      const d1 = data[index];
      const d = date - d0.date > d1.date - date ? d1 : d0;
      setMousePosition({ x, y, value: d.value });
    });

    svg.on('mouseout', () => {
      setMousePosition(null);
    });
  }, [data]);

  return (
    <div ref={svgRef}></div>
  );
};

export default LineGraph;