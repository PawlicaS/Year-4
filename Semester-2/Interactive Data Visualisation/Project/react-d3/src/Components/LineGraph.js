import React, { useRef, useEffect } from 'react';
import * as d3 from 'd3';

const LineGraph = ({data}) => {
  const graphRef = useRef(null);

  useEffect(() => {
    d3.select("svg").remove();
    var retailData = [];
    var keyshopsData = [];
    for (var i = 0; i < data.chartData.retail.length; i++) {
      if (data.chartData.retail[i].y != null) {
        if (data.chartData.retail[i].x != null) {
          retailData.push({x: i, y: data.chartData.retail[i].y})
        }
      }
    }
    for (var i = 0; i < data.chartData.keyshops.length; i++) {
      if (data.chartData.keyshops[i].y != null) {
        if (data.chartData.keyshops[i].x != null) {
          keyshopsData.push({x: i, y: data.chartData.keyshops[i].y})
        }
      }
    }
    console.log(retailData);
    const margin = { top: 20, right: 20, bottom: 30, left: 30 };
    const width = 1200 - margin.left - margin.right;
    const height = 600 - margin.top - margin.bottom;

    const x = d3.scaleLinear()
      .range([0, width])
      .domain([0, d3.max(retailData, d => d.x)]);

    const y = d3.scaleLinear()
      .range([height, 0])
      .domain([0, d3.max(retailData, d => d.y)]);
      
    const line1 = d3.line()
      .x(d => x(d.x))
      .y(d => y(d.y));

    const line2 = d3.line()
      .x(d => x(d.x))
      .y(d => y(d.y));

    const svg = d3.select(graphRef.current)
      .append('svg')
        .attr('width', width + margin.left + margin.right)
        .attr('height', height + margin.top + margin.bottom)
      .append('g')
        .attr('transform', `translate(${margin.left}, ${margin.top})`);

    svg.append('g')
        .attr('transform', `translate(0, ${height})`)
        .call(d3.axisBottom(x));

    svg.append('g')
        .call(d3.axisLeft(y));

    svg.append('path')
        .datum(retailData)
        .attr('fill', 'none')
        .attr('stroke', 'steelblue')
        .attr('stroke-width', 1.5)
        .attr('d', line1);

    svg.append('path')
        .datum(keyshopsData)
        .attr('fill', 'none')
        .attr('stroke', 'orange')
        .attr('stroke-width', 1.5)
        .attr('d', line2);
  }, [data]);

  return (
    <div ref={graphRef}></div>
  );
};

export default LineGraph;