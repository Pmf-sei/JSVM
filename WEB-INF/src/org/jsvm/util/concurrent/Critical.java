/**

Copyright 2010-2011, The JSVM Project. 
All rights reserved.
 
Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:
  
1. Redistributions of source code must retain the above copyright notice, 
   this list of conditions and the following disclaimer.
        
2. Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the 
   documentation and/or other materials provided with the distribution.
        
3. Neither the name of the JSVM nor the names of its contributors may be 
   used to endorse or promote products derived from this software 
   without specific prior written permission.
        
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
OF THE POSSIBILITY OF SUCH DAMAGE.

 *
 * Author: Hu Dong
 * Contact: jsvm.prj@gmail.com
 * License: BSD 3-Clause License
 * Source code availability: http://jzvm.googlecode.com
 */
package org.jsvm.util.concurrent;

import java.util.concurrent.Semaphore;

/**
 * 
 *
 */
public final class Critical {
	private Semaphore S;

	private static final ThreadLocal<Integer> counter = new ThreadLocal<Integer>();

	public Critical(int concurrent) {
		if (concurrent <= 0)
			throw new IllegalArgumentException("The concurrent number must > 0");

		S = new Semaphore(concurrent);
	}

	public Critical() {
		this(1);
	}
	
	/**
	 * Enter critical segment
	 */
	public void enter(){
		Integer i = counter.get();
		if(i == null){
			i = 0;
		}
		
		if(i == 0){
			S.acquireUninterruptibly();
		}
		
		counter.set(i++);
	}
	
	/**
	 * Exit critical segment
	 */
	public void exit(){
		Integer i = counter.get();
		
		if(i > 1){
			counter.set(i--);
		}
		
		if(i == 0){
			S.release();
		}
	}
}
