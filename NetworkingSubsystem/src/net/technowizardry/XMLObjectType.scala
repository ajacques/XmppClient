package net.technowizardry

object XMLObjectType extends Enumeration {
	type XMLObjectType = Value
	val Document, EndDocument, StartElement, EndElement, Text = Value
}