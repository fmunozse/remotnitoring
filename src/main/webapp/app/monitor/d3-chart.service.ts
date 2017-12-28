declare const d3, nv: any;

/**
* ChartService to define the chart config for D3
*/

export class D3ChartService {

    static getChartConfig() {
        return {
            chart: {
                type: 'lineChart',
                height: 200,
                margin: {
                    top: 20,
                    right: 20,
                    bottom: 40,
                    left: 55
                },
                x(d) {
                    return (d) ? d.x : null;
                },
                y(d) {
                    return (d) ? d.y : null;
                },
                useInteractiveGuideline: true,
                dispatch: {},
                xAxis: {
                    axisLabel: 'Dates',
                    // showMaxMin: true,
                    tickFormat(d) {
                        return d3.time.format('%H:%M:%S')(new Date(d));
                    }
                },
                yAxis: {
                    axisLabel: '',
                    axisLabelDistance: 30
                },
                yDomain: [0, 1.1],
                transitionDuration: 250
            },
            title: {
                enable: true
            }
        };
    }
}
