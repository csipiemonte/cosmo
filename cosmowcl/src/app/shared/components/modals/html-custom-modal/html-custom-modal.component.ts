/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, OnInit, ViewEncapsulation, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-html-custom-modal',
  templateUrl: './html-custom-modal.component.html',
  styleUrls: ['./html-custom-modal.component.scss'],
  encapsulation: ViewEncapsulation.None
})

export class HtmlCustomModalComponent implements OnInit {
  @Input() html!: string;
  @Input() title!: string;
  safeHtml: any;

  constructor(
    public modal: NgbActiveModal,
    private sanitizer: DomSanitizer,
    public activeModal: NgbActiveModal
  ) {

  }

  ngOnInit(): void {
    this.safeHtml = this.sanitizer.bypassSecurityTrustHtml(this.html);
  }

  

}
