/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-creazione-pratica',
  templateUrl: './creazione-pratica.component.html',
  styleUrls: ['./creazione-pratica.component.scss']
})
export class CreazionePraticaComponent implements OnInit {

  constructor(
    private router: Router,
  ) { }

  ngOnInit(): void {
  }

  goHome() {
    this.router.navigate(['home']);
  }
}
