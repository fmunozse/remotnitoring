import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { MonitorNodeDTO } from './monitorNodeDTO.model';
import { ResponseWrapper, createRequestOption } from '../shared';

@Injectable()
export class MonitorService {

    private resourceUrl = SERVER_API_URL + 'api/monitor';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    query(req?: any): Observable<ResponseWrapper> {

        return this.http.get(`${this.resourceUrl}/last-situation`)
            .map((res: Response) => this.convertResponse(res));
    }

    queryLastestMonitorNode(req?: any): Observable<Response> {
        return this.http.get(`${this.resourceUrl}/lastest-monitorNode`);
    }

    queryFindHeardbeatsByDatePerNode(req: any): Observable<Response> {
        const params: URLSearchParams = new URLSearchParams();
        params.set('fromDate', req.fromDate);
        params.set('toDate', req.toDate);

        const options = {
            search: params
        };

        return this.http.get(`${this.resourceUrl}/heartbeatsByDatePerNode`, options);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to MonitorNodeDTO.
     */
    private convertItemFromServer(json: any): MonitorNodeDTO {
        const entity: MonitorNodeDTO = Object.assign(new MonitorNodeDTO(), json);
        entity.lastHeartbeat = this.dateUtils.convertDateTimeFromServer(json.lastHeartbeat);
        return entity;
    }

}
