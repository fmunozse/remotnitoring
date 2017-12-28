import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NvD3Module } from 'ng2-nvd3';

import { RemotnitoringSharedModule } from '../shared';

import 'd3';
import 'nvd3';

import { HOME_ROUTE,
         MonitorComponent,
         MonitorResolvePagingParams,
         MonitorService } from './';

@NgModule({
    imports: [
        RemotnitoringSharedModule,
        NvD3Module,
        RouterModule.forChild([ HOME_ROUTE ])
    ],
    declarations: [
        MonitorComponent,
    ],
    entryComponents: [
    ],
    providers: [
        MonitorService,
        MonitorResolvePagingParams
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RemotnitoringMonitorModule {}
