/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SimpleFormContainerComponent } from './simple-form-container/simple-form-container.component';
import { TextComponent } from './controls/text/text.component';
import { TextareaComponent } from './controls/textarea/textarea.component';
import { DateComponent } from './controls/date/date.component';
import { RadioComponent } from './controls/radio/radio.component';
import { CheckboxComponent } from './controls/checkbox/checkbox.component';
import { DropdownComponent } from './controls/dropdown/dropdown.component';
import { HeadlineComponent } from './controls/headline/headline.component';
import { SpacerComponent } from './controls/spacer/spacer.component';
import { HorizontalLineComponent } from './controls/horizontal-line/horizontal-line.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../shared/shared.module';
import { PeopleComponent } from './controls/people/people.component';



@NgModule({
  declarations: [
    SimpleFormContainerComponent,
    TextComponent,
    TextareaComponent,
    DateComponent,
    RadioComponent,
    CheckboxComponent,
    DropdownComponent,
    HeadlineComponent,
    SpacerComponent,
    HorizontalLineComponent,
    PeopleComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule
  ],
  exports: [
    SimpleFormContainerComponent
  ]
})
export class SimpleFormModule { }
