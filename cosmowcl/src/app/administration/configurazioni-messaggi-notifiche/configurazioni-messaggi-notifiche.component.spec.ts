/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigurazioniMessaggiNotificheComponent } from './configurazioni-messaggi-notifiche.component';

describe('ConfigurazioniMessaggiNotificheComponent', () => {
  let component: ConfigurazioniMessaggiNotificheComponent;
  let fixture: ComponentFixture<ConfigurazioniMessaggiNotificheComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfigurazioniMessaggiNotificheComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigurazioniMessaggiNotificheComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
