	package ru.ointeractive.jabadaba.exceptions;
	
	public class NotImplementedException extends Exception {
		
		public NotImplementedException (String method) {
			super (method + " not implemented");
		}
		
	}