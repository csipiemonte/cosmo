/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { ActivatedRoute, NavigationEnd, NavigationStart, Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { filter, scan } from 'rxjs/operators';
import { RouterHistory } from 'src/app/shared/models/router-history/routerHistory';
import { RouterHistoryInfo } from '../models/router-history/routerHistoryInfo';

@Injectable({
  providedIn: 'root'
})
export class RouterHistoryService {
  historyUrl$ = new BehaviorSubject<RouterHistoryInfo []>([]);
  root!: RouterHistoryInfo;
  history: RouterHistoryInfo [] = [];

  constructor(private route: ActivatedRoute, private router: Router) {
    router.events
      .pipe(

        // only include NavigationStart and NavigationEnd events
        filter(
          ((event: any) => event instanceof NavigationEnd || event instanceof NavigationStart)
        ),
        scan<NavigationStart | NavigationEnd, RouterHistory>(
          (acc, event) => {
            if (event instanceof NavigationStart) {
              // We need to track the trigger, id, and idToRestore from the NavigationStart events
              return {
                ...acc,
                event,
                trigger: event.navigationTrigger,
                id: event.id,
                idToRestore: (event.restoredState && event.restoredState.navigationId) ||
                  undefined
              };
            }
            // NavigationEnd events
            const history = [...acc.history];
            let currentIndex = acc.currentIndex;

            // router events are imperative (router.navigate or routerLink)
            if (acc.trigger === 'imperative') {
              // remove all events in history that come after the current index
              history.splice(currentIndex + 1);

              // add the new event to the end of the history and set that as our current index
              history.push({ id: acc.id, url: event.urlAfterRedirects,
                             data: route.root.firstChild?.snapshot.data,
                             filter: route.root.firstChild?.snapshot.queryParams.filter});
              currentIndex = history.length - 1;
            }

            // browser events (back/forward) are popstate events
            if (acc.trigger === 'popstate') {
              // get the history item that references the idToRestore
              const idx = history.findIndex(x => x.id === acc.idToRestore);

              // if found, set the current index to that history item and update the id
              if (idx > -1) {
                currentIndex = idx;
                history[idx].id = acc.id;
              } else {
                currentIndex = 0;
              }
            }

            return {
              ...acc,
              event,
              history,
              currentIndex
            };
          },
          {
            event: null,
            history: [],
            trigger: undefined,
            id: 0,
            idToRestore: 0,
            currentIndex: 0
          }
        ),
        // filter out so we only act when navigation is done
        filter(
          ({ event, trigger }) => event instanceof NavigationEnd && !!trigger
        )
      )
      .subscribe(({ history, currentIndex }: any) => {
        this.history = history;
        this.historyUrl$.next(this.history);
      });
  }

  back(){
    for (let i = this.history.length - 1; i >= 0; i--) {
      const init = this.history[i].url.split('?')[0];

      if (init && i > 0 && this.history[i - 1].url.startsWith(init)){
        this.history.pop();
      } else{
        break;
      }
    }

    this.history.pop();

    if (this.history.length > 0) {
        this.router.navigateByUrl(this.history[this.history.length - 1].url);
    } else {
      this.router.navigateByUrl('/');
    }
  }

  setRoot(rhi: RouterHistoryInfo) {
    this.root = rhi;
  }

  getPreviousUrl(rhi: RouterHistoryInfo[]): RouterHistoryInfo {

    const current = rhi[rhi.length - 1 ];
    const init = current.url.split('?')[0];

    let previousIndex = rhi.length - 2 ;

    for (let i = rhi.length - 2; i >= 0; i--) {
      if (rhi[i] && rhi[i].url && !('' + rhi[i].url).startsWith(init)){
        previousIndex = i;
        break;
      }
    }

    return rhi[previousIndex];
  }
}
