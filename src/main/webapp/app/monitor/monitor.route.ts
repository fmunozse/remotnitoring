import { Route } from '@angular/router';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { MonitorComponent } from './';

@Injectable()
export class MonitorResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const HOME_ROUTE: Route = {
    path: 'monitor',
    component: MonitorComponent,
    resolve: {
        'pagingParams': MonitorResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Monitor !!!! '
    },
    canActivate: [UserRouteAccessService]
};
