import React, { useRef, useEffect } from 'react';
import * as d3 from 'd3';

const LineGraph = ({data}) => {
  const graphRef = useRef(null);

  useEffect(() => {
    d3.select("svg").remove();
    const margin = { top: 20, right: 20, bottom: 30, left: 50 };
    const width = 600 - margin.left - margin.right;
    const height = 400 - margin.top - margin.bottom;

    const x = d3.scaleTime()
      .range([0, width])
      .domain(d3.extent(data, d => d.date));

    const y = d3.scaleLinear()
      .range([height, 0])
      .domain([0, d3.max(data, d => Math.max(d.value1, d.value2))]);

    const line1 = d3.line()
      .x(d => x(d.date))
      .y(d => y(d.value1));

    const line2 = d3.line()
      .x(d => x(d.date))
      .y(d => y(d.value2));

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
        .datum(data)
        .attr('fill', 'none')
        .attr('stroke', 'steelblue')
        .attr('stroke-width', 1.5)
        .attr('d', line1);

    svg.append('path')
        .datum(data)
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