<label><input type="radio"<#rt/>
 value="${rkey}"<#rt/>
<#if (rkey?string=="" && (!value?? || value?string=="")) || (value?? && value?string!="" && value?string==rkey?string)> checked="checked"</#if><#rt/>
<#include "ftl/component/common-attributes.ftl"/><#rt/>
<#include "ftl/component/scripting-events.ftl"/><#rt/>
/><@s.mt code=rvalue text=rvalue/></label><#if hasNext> </#if>