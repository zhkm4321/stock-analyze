<#--
<input type="number"/>
-->
<#macro number
	maxlength="" readonly="" value=""
	label="" noHeight="false" required="false" colspan="" width="" help="" helpPosition="2" colon=":" hasColon="true"
	id="" name="" class="" style="" size="" title="" disabled="" tabindex="" accesskey=""
	vld="" equalTo="" maxlength="" minlength="" max="" min="" rname="" rvalue=""
	onclick="" ondblclick="" onmousedown="" onmouseup="" onmouseover="" onmousemove="" onmouseout="" onfocus="" onblur="" onkeypress="" onkeydown="" onkeyup="" onselect="" onchange=""
	>
<#include "ftl/component/control.ftl"/><#rt/>
<input type="number"<#rt/>
<#if id!=""> id="${id}"</#if><#rt/>
<#if maxlength!=""> maxlength="${maxlength}"</#if><#rt/>
<#if max?string!=""> max="${max}"</#if><#rt/>
<#if min?string!=""> min="${min}"</#if><#rt/>
<#if readonly!=""> readonly="${readonly}"</#if><#rt/>
<#if rname!=""> rname="${rname}"</#if><#rt/>
<#if rvalue!=""> rvalue="${rvalue}"</#if><#rt/>
<#if title?? && title?string!=""> title="<@s.m '${title}'/>"</#if><#rt/>
<#if value?? && value?string!=""> value="${value?html}"</#if><#rt/>
<#if width?? && width?string!=""> width='${width}'<#else>width='100'</#if><#rt/>
<#include "ftl/component/common-attributes.ftl"/><#rt/>
<#include "ftl/component/scripting-events.ftl"/><#rt/>
/><#rt/>
<#include "ftl/component/control-close.ftl"/><#rt/>
</#macro>
