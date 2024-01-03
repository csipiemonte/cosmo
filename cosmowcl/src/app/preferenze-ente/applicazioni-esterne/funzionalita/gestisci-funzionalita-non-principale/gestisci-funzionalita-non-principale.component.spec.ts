/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { GestisciFunzionalitaNonPrincipaleComponent } from './gestisci-funzionalita-non-principale.component';


describe('GestisciFunzionalitaNonPrincipaleComponent', () => {
  let component: GestisciFunzionalitaNonPrincipaleComponent;
  let fixture: ComponentFixture<GestisciFunzionalitaNonPrincipaleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GestisciFunzionalitaNonPrincipaleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GestisciFunzionalitaNonPrincipaleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
