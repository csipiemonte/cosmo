/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioVerificaFirmeContainerComponent } from './dettaglio-verifica-firme-container.component';

describe('DettaglioVerificaFirmeContainerComponent', () => {
  let component: DettaglioVerificaFirmeContainerComponent;
  let fixture: ComponentFixture<DettaglioVerificaFirmeContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioVerificaFirmeContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioVerificaFirmeContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
