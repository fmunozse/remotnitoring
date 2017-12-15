import { Route } from '@angular/router';

import { MonitorComponent } from './';

export const HOME_ROUTE: Route = {
    path: 'monitor',
    component: MonitorComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Monitor !!!! '
    }
};
