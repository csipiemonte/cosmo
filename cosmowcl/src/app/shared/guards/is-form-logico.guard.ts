/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { PraticheService } from 'src/app/shared/services/pratiche.service';
import { SimpleFormService } from 'src/app/simple-form/services/simple-form.service';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { ModalService } from '../services/modal.service';

@Injectable()
export class IsFormLogicoGuard implements CanActivate {

    constructor(
        private simpleFormService: SimpleFormService,
        private router: Router) { }

    public canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.simpleFormService.getSimpleFormFromTaskId(route.params.id).pipe(
            map(simpleForm => {
                this.simpleFormService.storedSimpleForm = simpleForm;
                this.router.navigate(['/tasks/' + route.params.id + '/simple-form']);
                return false;
            })
        );
    }
}
