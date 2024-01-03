/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovazioneRifiutoComponent } from './approvazione-rifiuto.component';

describe('ApprovazioneRifiutoComponent', () => {
  let component: ApprovazioneRifiutoComponent;
  let fixture: ComponentFixture<ApprovazioneRifiutoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApprovazioneRifiutoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovazioneRifiutoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
