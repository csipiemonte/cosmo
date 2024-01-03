/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppEsterneComponent } from './app-esterne.component';

describe('AppEsterneComponent', () => {
  let component: AppEsterneComponent;
  let fixture: ComponentFixture<AppEsterneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppEsterneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppEsterneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
