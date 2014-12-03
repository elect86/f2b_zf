//--------------------------------------------------------------------------------------
//			** Z-fighting Aware Depth Peeling **
//  
//  Descpiption: 
//     (1) Extract the color of the fragment with the maximum ID.
//     (2) Calculate next depth layer.
//
//  Authors: Andreas A. Vasilakis - Ioannis Fudos
//  Emails : abasilak@cs.uoi.gr - fudos@cs.uoi.gr
// 
//  Software is distributed under the following BSD-style license:
//
//  Copyright © 2010, 2011 University of Ioannina. All Rights Reserved.
//
//  Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
//
//  1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
//
//  2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
//
//  3. The name of the author may not be used to endorse or promote products derived from this software without specific prior written permission.
//
//  THIS SOFTWARE IS PROVIDED BY THE CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
//  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
//  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
//  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
//  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 
//  IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
//  The work was partially supported by a Heraclitus II grant through the operational programme "Education and Lifelong Learning"
//  which is co-financed by Greece and the European Union through the European Social Fund.
//--------------------------------------------------------------------------------------

#version 330 core
#extension GL_EXT_gpu_shader4 : enable

//vec4 computePixelColor();

uniform sampler2DRect tex_depth;
uniform sampler2DRect tex_count_id;
uniform vec4 color;

layout(location = 0, index = 0) out  vec4 out_frag_depth;
layout(location = 1, index = 0) out  vec4 out_frag_color;

void main(void)
{
	ivec2	count_id;
	float	depth;

	depth	 =	    -texture(tex_depth	 , gl_FragCoord.xy).r;
	count_id = ivec2(texture(tex_count_id, gl_FragCoord.xy).ra);
	
	if (gl_FragCoord.z < depth) 
		discard;

	//out_frag_color = (gl_PrimitiveID == count_id.y) ? computePixelColor() : vec4(0.0f);
	out_frag_color = (gl_PrimitiveID == count_id.y) ? color : vec4(0.0f);

	if     (count_id.x <= 1 && gl_FragCoord.z == depth)  
	
		out_frag_depth.r = -1.0f;
		
	else if(count_id.x >  1 && gl_FragCoord.z >  depth)  
	
		out_frag_depth.r = -1.0f;
		
	else
		out_frag_depth.r = -gl_FragCoord.z;
}