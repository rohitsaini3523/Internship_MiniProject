import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  registerForm = this.fb.group({
    userName: ['', [Validators.required , Validators.pattern("^[A-Za-z]\\w{5, 29}$")]],
    password : ['', Validators.required],
    email: ['', [Validators.required, Validators.email]]
  })




  constructor(private fb: FormBuilder) { }

    get userName() {

    return this.registerForm.controls['userName'];


  } 
  get password() {
    return this.registerForm.controls['password'];
  }

  get email() {
    return this.registerForm.controls['email'];
  }

}
