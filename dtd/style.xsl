<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body>
	<h1>WorkflowManager</h1>
    <h2>Workflows</h2>
	<xsl:for-each select="WorkflowManager/workflow">
		<h3><xsl:value-of select="name"/></h3>
		<table border="1">
			<tr bgcolor="#9acd32">
				<th align="left">Action Name</th>
				<th align="left">Role</th>
				<th align="left">Next Actions</th>
			</tr>
			
			<xsl:for-each select="WorkflowManager/workflow/action">
			<tr>
				<td><xsl:value-of select="name"/></td>
				<td><xsl:value-of select="role"/></td>
				<td><xsl:for-each select="WorkflowManager/workflow/action/simple_action">
						<xsl:value-of select="nextActions"/>
				</xsl:for-each></td>
				<td><xsl:for-each select="WorkflowManager/workflow/action/process_action">
						<xsl:value-of select="nextProcess"/>
				</xsl:for-each></td>
			</tr>
			</xsl:for-each>
		</table>
	</xsl:for-each>

  </body>
  </html>
</xsl:template>

</xsl:stylesheet>