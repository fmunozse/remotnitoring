import { Component, OnInit, OnDestroy } from '@angular/core';
import { EchoService } from '../shared';

@Component({
  selector: 'jhi-monitor',
  templateUrl: './monitor.component.html',
  styles: []
})
export class MonitorComponent implements OnInit, OnDestroy {

  echos: any[] = [];

  constructor(
    private echoService: EchoService
  ) { }

  ngOnInit() {
    this.echoService.subscribe();
    this.echoService.receive().subscribe((echo) => {
        this.showEchos(echo);
    });
  }

  ngOnDestroy() {
    this.echoService.unsubscribe();
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
