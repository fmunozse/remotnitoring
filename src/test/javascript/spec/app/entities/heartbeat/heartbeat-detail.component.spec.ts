/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { RemotnitoringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { HeartbeatDetailComponent } from '../../../../../../main/webapp/app/entities/heartbeat/heartbeat-detail.component';
import { HeartbeatService } from '../../../../../../main/webapp/app/entities/heartbeat/heartbeat.service';
import { Heartbeat } from '../../../../../../main/webapp/app/entities/heartbeat/heartbeat.model';

describe('Component Tests', () => {

    describe('Heartbeat Management Detail Component', () => {
        let comp: HeartbeatDetailComponent;
        let fixture: ComponentFixture<HeartbeatDetailComponent>;
        let service: HeartbeatService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RemotnitoringTestModule],
                declarations: [HeartbeatDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    HeartbeatService,
                    JhiEventManager
                ]
            }).overrideTemplate(HeartbeatDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(HeartbeatDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HeartbeatService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Heartbeat(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.heartbeat).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
