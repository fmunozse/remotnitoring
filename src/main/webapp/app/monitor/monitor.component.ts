import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { EchoService } from '../shared';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../shared';

import { MonitorNodeDTO } from './monitorNodeDTO.model';
import { MonitorService } from './monitor.service';

@Component({
  selector: 'jhi-monitor',
  templateUrl: './monitor.component.html',
  styles: []
})
export class MonitorComponent implements OnInit, OnDestroy {

  //
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  routeData: any;
  itemsPerPage: any;

  //
  monitorNodeDTOs: MonitorNodeDTO[];
  echos: any[] = [];

  constructor(
    private parseLinks: JhiParseLinks,
    private jhiAlertService: JhiAlertService,
    private principal: Principal,
    private activatedRoute: ActivatedRoute,
    private dataUtils: JhiDataUtils,
    private router: Router,
    private eventManager: JhiEventManager,

    private echoService: EchoService,
    private monitorService: MonitorService
  ) {
    /*
      this.itemsPerPage = ITEMS_PER_PAGE;
      this.routeData = this.activatedRoute.data.subscribe((data) => {
        this.page = data['pagingParams'].page;
        this.previousPage = data['pagingParams'].page;
        this.reverse = data['pagingParams'].ascending;
        this.predicate = data['pagingParams'].predicate;
      });
    */
  }

  ngOnInit() {
    this.loadAll();
    this.echoService.subscribe();
    this.echoService.receive().subscribe((echo) => {
        this.showEchos(echo);
    });
  }

  ngOnDestroy() {
    this.echoService.unsubscribe();
  }

  loadAll() {
    this.monitorService.query().subscribe(
        (res: ResponseWrapper) => this.onSuccessQueryMonitorNodeDTO(res.json, res.headers),
        (res: ResponseWrapper) => this.onError(res.json)
    );
  }

  private onSuccessQueryMonitorNodeDTO(data, headers) {
    // this.links = this.parseLinks.parse(headers.get('link'));
    // this.totalItems = headers.get('X-Total-Count');
    // this.queryCount = this.totalItems;
    // this.page = pagingParams.page;
    this.monitorNodeDTOs = data;
    console.log('data', data);
    console.log('headers', headers);
  }

  private onError(error) {
      this.jhiAlertService.error(error.message, null, null);
  }

  showEchos(echo: any) {
    console.log(echo);
    this.echos.push(echo);
  }

  startWebSocket() {
    console.log('start')
    this.echoService.connect();
  }

  stopWebSocket() {
    console.log('stop')
    this.echoService.disconnect();
  }

}
