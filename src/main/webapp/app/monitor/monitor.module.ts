import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RemotnitoringSharedModule } from '../shared';

import { HOME_ROUTE,
         MonitorComponent,
         MonitorResolvePagingParams,
         MonitorService } from './';

@NgModule({
    imports: [
        RemotnitoringSharedModule,
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
