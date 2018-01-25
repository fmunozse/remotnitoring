/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { RemotnitoringTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RequestRemoteCommandDetailComponent } from '../../../../../../main/webapp/app/entities/request-remote-command/request-remote-command-detail.component';
import { RequestRemoteCommandService } from '../../../../../../main/webapp/app/entities/request-remote-command/request-remote-command.service';
import { RequestRemoteCommand } from '../../../../../../main/webapp/app/entities/request-remote-command/request-remote-command.model';

describe('Component Tests', () => {

    describe('RequestRemoteCommand Management Detail Component', () => {
        let comp: RequestRemoteCommandDetailComponent;
        let fixture: ComponentFixture<RequestRemoteCommandDetailComponent>;
        let service: RequestRemoteCommandService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [RemotnitoringTestModule],
                declarations: [RequestRemoteCommandDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RequestRemoteCommandService,
                    JhiEventManager
                ]
            }).overrideTemplate(RequestRemoteCommandDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RequestRemoteCommandDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RequestRemoteCommandService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new RequestRemoteCommand(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.requestRemoteCommand).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
