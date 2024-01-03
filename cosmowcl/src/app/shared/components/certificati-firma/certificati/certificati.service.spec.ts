/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { TestBed } from '@angular/core/testing';

import { CertificatiService } from './certificati.service';

describe('CertificatiService', () => {
  let service: CertificatiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CertificatiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
