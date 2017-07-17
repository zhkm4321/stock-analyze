<label><input type="checkbox"<#rt/>
 value="${rkey}"<#rt/>
<#if valueList?seq_contains(rkey)> checked="checked"</#if><#rt/>
<#include "ftl/component/common-attributes.ftl"/><#rt/>
<#include "ftl/component/scripting-events.ftl"/><#rt/>
/>${rvalue}</label><#if hasNext> </#if>