import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatusStoreComponent } from './status-store.component';

describe('StatusStoreComponent', () => {
  let component: StatusStoreComponent;
  let fixture: ComponentFixture<StatusStoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatusStoreComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatusStoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
