import {  Validators, AbstractControl, ValidationErrors } from '@angular/forms';

export function requiredIfValidator(predicate:Predicate)  {
  
  return (formControl:AbstractControl) : ValidationErrors | null => {
    if (!formControl.parent) {
      return null;
    }
    if (predicate()) {
		return Validators.required(formControl); 
    } else {
		return null;
	}
  };
}

export type Predicate = () => boolean;