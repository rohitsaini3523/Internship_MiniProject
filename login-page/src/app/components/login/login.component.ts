// import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Credentials } from '../../model/credentials';
import { BackendService } from '../../services/backend/backend.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  

  credentials!: Credentials

  loginForm: FormGroup = new FormGroup ({
    // dbt
    username : new FormControl(''),
    password : new FormControl('')
  })



  constructor(private fb: FormBuilder , private bs: BackendService) { }
     login() {
      this.credentials = new Credentials();
      this.credentials.username =this.loginForm.get("username")?.value;
      this.credentials.password =this.loginForm.get("password")?.value;
      this.bs.newLogin(this.credentials)}
  

}
