import { Component, OnInit } from '@angular/core';
import {UserService} from "../user.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  role = '';
  showUserProfile: boolean = false;
  constructor(private appService: UserService) { }

  ngOnInit(): void {
    this.role = this.appService.role;
  }

}
