import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginForm = this.fb.group({
    // dbt
    username : [''],
    password : ['']
  })


  constructor(private fb: FormBuilder) {
     
  }

}
