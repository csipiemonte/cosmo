/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { TestBed } from '@angular/core/testing';

import { DiscoveryService } from './discovery.service';

describe('DiscoveryService', () => {
  let service: DiscoveryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DiscoveryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
