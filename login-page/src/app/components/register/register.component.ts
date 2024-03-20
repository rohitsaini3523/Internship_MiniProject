import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router'; 
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  registerForm = this.fb.group({
    fullname: ['', [Validators.required, Validators.pattern("^[A-Za-z]\\w{5,29}$")]],
    password : ['', Validators.required],
    confirm_password: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]]
  });

  submitted = false;

  constructor(private fb: FormBuilder ,private router: Router) { }

  get formControls() {
    return this.registerForm.controls;
  }

  get fullname() {  
    return this.registerForm.get('fullname');
  }

  register() {
    this.submitted = true;

    if (this.registerForm.invalid) {
      return;
    }

    this.router.navigate(['/login']);
  }

  submitdetails() {
    console.log(this.registerForm.value)
  }
}
