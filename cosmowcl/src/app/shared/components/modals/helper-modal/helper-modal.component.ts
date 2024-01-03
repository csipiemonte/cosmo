/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-helper-modal',
  templateUrl: './helper-modal.component.html',
  styleUrls: ['./helper-modal.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HelperModalComponent implements OnInit {
  @Input() html!: string;
  @Input() title!: string;
  safeHtml: any;

  constructor(
    public modal: NgbActiveModal,
    private sanitizer: DomSanitizer
  ) {

  }

  ngOnInit(): void {
    this.safeHtml = this.sanitizer.bypassSecurityTrustHtml(this.html);
  }

}
