import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils, JhiDateUtils } from 'ng-jhipster';

import { EchoService } from '../shared';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../shared';

import { D3ChartService } from './d3-chart.service';
import { MonitorNodeDTO } from './monitorNodeDTO.model';
import { MonitorService } from './monitor.service';

@Component({
  selector: 'jhi-monitor',
  templateUrl: './monitor.component.html',
  styleUrls: [
    'monitor.css'
  ]
})
export class MonitorComponent implements OnInit, OnDestroy {
  //
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  routeData: any;
  itemsPerPage: any;
  datePipe: DatePipe;

  //
  nodeCards: MonitorNodeDTO[];
  echos: MonitorNodeDTO[] = [];

  // Charts
  @ViewChild('nvd3') nvd3Chart;
  pingChart: any;
  pingChartOptions: any;
  pingChartData: any = [];

  constructor(
    private parseLinks: JhiParseLinks,
    private jhiAlertService: JhiAlertService,
    private principal: Principal,
    private activatedRoute: ActivatedRoute,
    private dataUtils: JhiDataUtils,
    private dateUtils: JhiDateUtils,
    private router: Router,
    private eventManager: JhiEventManager,

    private echoService: EchoService,
    private monitorService: MonitorService

  ) {
      this.itemsPerPage = ITEMS_PER_PAGE;
      this.routeData = this.activatedRoute.data.subscribe((data) => {
        this.page = data['pagingParams'].page;
        this.previousPage = data['pagingParams'].page;
        this.reverse = data['pagingParams'].ascending;
        this.predicate = data['pagingParams'].predicate;
      });
      this.datePipe = new DatePipe('en');
  }

  ngOnInit() {
    console.log('ngOnInit')
    this.loadAll();
    this.startWebSocket();
    this.echoService.subscribe();
    this.echoService.receive().subscribe((echo) => {
        this.showEchos(echo);
        this.updateFromTopicNodeCards(echo);
    });
  }

  ngOnDestroy() {
    console.log('ngOnDestroy')
    this.echoService.unsubscribe();
    this.stopWebSocket();
  }

  loadAll() {
    // Sumarization
    this.monitorService.query().subscribe((res: ResponseWrapper) => {
          this.nodeCards = res.json;
    });

    // Data for chart
    this.monitorService.queryLastest4hMonitorNode().subscribe((res: ResponseWrapper) => {
        this.pingChart = res.json ();
        this.printChart();
        // console.log('pingChartData', this.pingChartData);
    });
  }

  private printChart() {
    const startDate = new Date();
    startDate.setHours(startDate.getHours() - 4);

    this.pingChartOptions = {... D3ChartService.getChartConfig() };
    if (this.pingChart) {
      this.pingChartOptions.title.text = 'titulo';
      this.pingChartOptions.chart.yAxis.axisLabel = 'Ping';

      // Iterate all nodes
      for (const nodeName of Object.keys(this.pingChart)) {
        const valuesSerie = [];

        // First element
        valuesSerie.push(this.prepareElement(startDate, 0, 0));

        // In case that no heartbeats, just add a entry with current date
        if (this.pingChart[nodeName].length === 0) {
          valuesSerie.push(this.prepareElement(new Date(), 0, 0));

        } else {
          // When find several heartbeats iterate over all and add the entries in the chart.
          this.pingChart[nodeName].forEach((item) => {
            this.addElementToChart (item, valuesSerie);
          }); // End iterate heartbeats of node
        }

        // Add data
        this.pingChartData.push({
          values: valuesSerie,
          key: nodeName
        });

      }; // End iterate node
    };
  }

  private showEchos(echo: MonitorNodeDTO) {
    console.log(echo);
    this.echos.unshift(echo);
    let counter = 0;
    this.pingChartData.forEach( (serie) => {
      if (serie.key === echo.nodeName) {
        this.addElementToChart(echo, this.pingChartData[counter].values);
      } else {
        this.pingChartData[counter].values.push(this.prepareElement(new Date(), 0, 0));
      }
      counter++;
    });
    this.nvd3Chart.chart.update();
  }

  private updateFromTopicNodeCards(echo: MonitorNodeDTO) {
    this.nodeCards.forEach( (monitorNodeDTO) => {
      if (monitorNodeDTO.nodeId === echo.nodeId) {
        monitorNodeDTO.numHeartbeats += echo.numHeartbeats ;
        monitorNodeDTO.lastHeartbeat = echo.lastHeartbeat;
      }
    });
  }

  private startWebSocket() {
    // console.log('start')
    this.echoService.connect();
  }

  private stopWebSocket() {
    // console.log('stop')
    this.echoService.disconnect();
  }

  private prepareElement(date: Date, seconds: number, yValue: number) {
    return {
      x: new Date(date.getTime() + (seconds * 1000)),
      y: yValue
    };
  }

  private addElementToChart(item, valuesSerie) {
    const timestamp: Date = this.dateUtils.convertDateTimeFromServer(item.lastHeartbeat);
    valuesSerie.push(this.prepareElement(timestamp, -30, 0));
    valuesSerie.push(this.prepareElement(timestamp, 0, 1));
    valuesSerie.push(this.prepareElement(timestamp, 30, 0));
  }

}
