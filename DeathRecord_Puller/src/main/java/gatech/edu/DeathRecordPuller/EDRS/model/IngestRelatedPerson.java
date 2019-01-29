/*
 * EDRS Submission API
 * This is the swagger documentation for DeathDataHub's EDRS Submission API. This API's purpose is to allow Medical Examiners and coroners to submit a signed IngestDeathRecord to a remote Electronic Death Record System. Although this is part of the deathdatahub main platform, the parameters are complex enough to warrent it's own swagger doc file
 *
 * OpenAPI spec version: 1.0.0
 * Contact: Michael.Riley@gtri.gatech.edu
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package gatech.edu.DeathRecordPuller.EDRS.model;

import java.util.Objects;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * RelatedPerson
 */
public class IngestRelatedPerson {
	/**
	 * Gets or Sets type
	 */
	@JsonAdapter(TypeEnum.Adapter.class)
	public enum TypeEnum {
		SPOUSE("Spouse"),

		MOTHER("Mother"),

		FATHER("Father"),

		INFORMANT("Informant"),

		OTHER("Other");

		private String value;

		TypeEnum(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static TypeEnum fromValue(String text) {
			for (TypeEnum b : TypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}

		public static class Adapter extends TypeAdapter<TypeEnum> {
			@Override
			public void write(final JsonWriter jsonWriter, final TypeEnum enumeration) throws IOException {
				jsonWriter.value(enumeration.getValue());
			}

			@Override
			public TypeEnum read(final JsonReader jsonReader) throws IOException {
				String value = jsonReader.nextString();
				return TypeEnum.fromValue(String.valueOf(value));
			}
		}
	}

	@SerializedName("type")
	private TypeEnum type = null;

	@SerializedName("name")
	private String name = null;

	@SerializedName("address")
	private IngestAddress address = null;

	@SerializedName("telecom")
	private String telecom = null;

	public IngestRelatedPerson type(TypeEnum type) {
		this.type = type;
		return this;
	}

	/**
	 * Get type
	 * 
	 * @return type
	 **/

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	public IngestRelatedPerson name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get name
	 * 
	 * @return name
	 **/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IngestRelatedPerson address(IngestAddress address) {
		this.address = address;
		return this;
	}

	/**
	 * Get address
	 * 
	 * @return address
	 **/

	public IngestAddress getAddress() {
		return address;
	}

	public void setAddress(IngestAddress address) {
		this.address = address;
	}

	public IngestRelatedPerson telecom(String telecom) {
		this.telecom = telecom;
		return this;
	}

	/**
	 * Get telecom
	 * 
	 * @return telecom
	 **/

	public String getTelecom() {
		return telecom;
	}

	public void setTelecom(String telecom) {
		this.telecom = telecom;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		IngestRelatedPerson relatedPerson = (IngestRelatedPerson) o;
		return Objects.equals(this.type, relatedPerson.type) && Objects.equals(this.name, relatedPerson.name)
				&& Objects.equals(this.address, relatedPerson.address)
				&& Objects.equals(this.telecom, relatedPerson.telecom);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, name, address, telecom);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class RelatedPerson {\n");

		sb.append("    type: ").append(toIndentedString(type)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    address: ").append(toIndentedString(address)).append("\n");
		sb.append("    telecom: ").append(toIndentedString(telecom)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
