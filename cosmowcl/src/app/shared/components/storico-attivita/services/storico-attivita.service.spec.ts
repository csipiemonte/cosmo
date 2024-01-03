/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { TestBed } from '@angular/core/testing';

import { StoricoAttivitaService } from './storico-attivita.service';

describe('CommentiService', () => {
  let service: StoricoAttivitaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StoricoAttivitaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
