/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DettaglioInformazioniFeaComponent } from './dettaglio-informazioni-fea.component';

describe('DettaglioInformazioniFeaComponent', () => {
  let component: DettaglioInformazioniFeaComponent;
  let fixture: ComponentFixture<DettaglioInformazioniFeaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DettaglioInformazioniFeaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioInformazioniFeaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
