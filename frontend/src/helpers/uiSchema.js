const uiSchema = {
  "Sending Application": {
    "ui:widget": "customInputWidget"
  },
  Provider: {
    "ui:options":  {
      addable: true,
      removable: true,
      orderable: false
    },
    items: {
      "ui:field": "grid",
      "ui:options": {
        "order": [
          {
            ID: { md: 3 },
            Name: { md: 3 },
            Phone: { md: 3 },
            Fax: { md: 3 },
          },
          {
            Email: { md: 3 },
            Facility: { md: 3 },
            Country: { md: 3 },
            Address: { md: 3 }
          }
        ]
      },
      ID: {
        classNames: "header",
        "ui:field": "grid",
        "ui:options": {
          "order": [
            {
              value: { md: 6 },
              type: { md: 6 }
            }
          ]
        },
        value: {
          "ui:widget": "customInputWidget"
        },
        type: {
          "ui:widget": "customInputWidget"
        }
      },
      Name: {
        classNames: "header",
        "ui:widget": "customInputWidget"
      },
      Phone: {
        classNames: "header",
        "ui:widget": "customInputWidget"
      },
      Fax: {
        classNames: "header",
        "ui:widget": "customInputWidget"
      },
      Email: {
        classNames: "header",
        "ui:widget": "customInputWidget"
      },
      Facility: {
        classNames: "header",
        "ui:widget": "customInputWidget"
      },
      Address: {
        classNames: "header",
        "ui:widget": "customInputWidget"
      },
      Country: {
        classNames: "header",
        "ui:widget": "customInputWidget"
      }
    }
  },
  Patient: {
    "ui:field": "grid",
    "ui:options": {
      "order": [
        {
          ID: { md: 3 }
        },
        {
          Name: { md: 3 }
        },
        {
          Sex: { md: 3 },
          Birth_Date: { md: 3 },
          Street_Address: { md: 3 }
        }, {
          Parents_Guardians: { md: 3 }
        },{
          Ethnicity: { md: 3 },
          Preferred_Language: { md: 3 },
          Race: { md: 3 }
        }, {
          PatientClass: { md: 3 },
          Occupation: { md: 3 },
          Pregnant: { md: 3 },
        }, {
          Travel_History: { md: 3 },
          Insurance_Type: { md: 3 },
          Visit_DateTime: { md: 3 }
        }, {
          Immunization_History: { md: 3 }
        }, {
          Admission_DateTime: { md: 3 },
          Date_of_Onset: { md: 3 }
        }, {
          Symptoms: { md: 3 }
        }, {
          Lab_Order_Code: { md: 3 }
        }
      ]
    },
    ID: {
      classNames: "header",
      "ui:options":  {
        addable: true,
        removable: true,
        orderable: false,
        addButtonText: "Add ID",
        removeButtonText: "Remove ID"
      },
      items: {
        "ui:field": "grid",
        "ui:options": {
          order: [
            {
              value: { md: 3 },
              type: { md: 3 }
            }
          ]
        },
        value: {
          "ui:widget": "customInputWidget"
        },
        type: {
          "ui:widget": "customInputWidget"
        }
      }
    },
    Name: {
      classNames: "header",
      "ui:field": "grid",
      "ui:options": {
        order: [
          {
            given: { md: 3 },
            family: { md: 3 }
          }
        ]
      },
      given: {
        "ui:widget": "customInputWidget"
      },
      family: {
        "ui:widget": "customInputWidget"
      }
    },
    Parents_Guardians: {
      classNames: "header",
      "ui:options":  {
        addable: true,
        removable: true,
        orderable: false,
        addButtonText: "Add Parent",
        removeButtonText: "Remove Parent"
      },
      items: {
        "ui:field": "grid",
        "ui:options": {
          order: [
            {
              Name: { md: 3 },
              Phone: { md: 3 },
              Email: { md: 3 }
            }
          ]
        },
        Name: {
          classNames: "no-title",
          given: {
            "ui:widget": "customInputWidget"
          },
          family: {
            "ui:widget": "customInputWidget"
          }
        },
        Phone: {
          "ui:widget": "customInputWidget"
        },
        Email: {
          "ui:widget": "customInputWidget"
        }
      }
    },
    Street_Address: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Birth_Date: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Sex: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    PatientClass: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Race: {
      classNames: "header",
      Code: {
        "ui:widget": "customInputWidget"
      },
      System: {
        "ui:widget": "customInputWidget"
      },
      Display: {
        "ui:widget": "customInputWidget"
      }
    },
    Ethnicity: {
      classNames: "header",
      Code: {
        "ui:widget": "customInputWidget"
      },
      System: {
        "ui:widget": "customInputWidget"
      },
      Display: {
        "ui:widget": "customInputWidget"
      }
    },
    Preferred_Language: {
      classNames: "header",
      Code: {
        "ui:widget": "customInputWidget"
      },
      System: {
        "ui:widget": "customInputWidget"
      },
      Display: {
        "ui:widget": "customInputWidget"
      }
    },
    Occupation: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Pregnant: {
      classNames: "header"
    },
    Travel_History: {
      classNames: "header",
      "ui:options":  {
        addable: true,
        removable: true,
        orderable: false,
        addButtonText: "Add Travel History",
        removeButtonText: "Remove Travel History"
      },
      items: {

      }
    },
    Insurance_Type: {
      classNames: "header",
      Code: {
        "ui:widget": "customInputWidget"
      },
      System: {
        "ui:widget": "customInputWidget"
      },
      Display: {
        "ui:widget": "customInputWidget"
      }
    },
    Immunization_History: {
      classNames: "header",
      "ui:options":  {
        addable: true,
        removable: true,
        orderable: false,
        addButtonText: "Add Immunization",
        removeButtonText: "Remove Immunization"
      },
      items: {
        "ui:field": "grid",
        "ui:options": {
          order: [
            {
              Code: { md: 3 },
              System: { md: 3 },
              Date: { md: 3 }
            }
          ]
        },
        Code: {
          "ui:widget": "customInputWidget"
        },
        System: {
          "ui:widget": "customInputWidget"
        },
        Date: {
          "ui:widget": "customInputWidget"
        }
      }
    },
    Visit_DateTime: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Admission_DateTime: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Date_of_Onset: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Symptoms: {
      classNames: "header",
      "ui:options":  {
        addable: true,
        removable: true,
        orderable: false,
        addButtonText: "Add Symptom",
        removeButtonText: "Remove Symptom"
      },
      items: {
        "ui:field": "grid",
        "ui:options": {
          order: [
            {
              Code: { md: 3 },
              System: { md: 3 },
              Display: { md: 3 }
            }
          ]
        },
        Code: {
          "ui:widget": "customInputWidget"
        },
        System: {
          "ui:widget": "customInputWidget"
        },
        Display: {
          "ui:widget": "customInputWidget"
        }
      }
    },
    Lab_Order_Code: {
      classNames: "header",
      "ui:options":  {
        addable: true,
        removable: true,
        orderable: false,
        addButtonText: "Add Lab Order",
        removeButtonText: "Remove Lab Order"
      },
      items: {
        "ui:field": "grid",
        "ui:options": {
          order: [
            {
              Code: { md: 3 },
              System: { md: 3 },
              Display: { md: 3 },
              Laboratory_Results: { md: 3 }
            }
          ]
        },
        Code: {
          "ui:widget": "customInputWidget"
        },
        System: {
          "ui:widget": "customInputWidget"
        },
        Display: {
          "ui:widget": "customInputWidget"
        },
        Laboratory_Results: {
          "ui:options":  {
            addable: true,
            removable: true,
            orderable: false,
            addButtonText: "Add Lab Result",
            removeButtonText: "Remove Lab Result"
          },
          items: {
            Value: {
              "ui:widget": "customInputWidget"
            },
            Unit: {
              Code: {
                "ui:widget": "customInputWidget"
              },
              System: {
                "ui:widget": "customInputWidget"
              },
              Display: {
                "ui:widget": "customInputWidget"
              }
            },
            Code: {
              "ui:widget": "customInputWidget"
            },
            System: {
              "ui:widget": "customInputWidget"
            },
            Display: {
              "ui:widget": "customInputWidget"
            }
          }
        }
      }
    },
    "Placer Order Code": {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Diagnosis: {
      classNames: "header",
      Code: {
        "ui:widget": "customInputWidget"
      },
      System: {
        "ui:widget": "customInputWidget"
      },
      Display: {
        "ui:widget": "customInputWidget"
      },
      Date: {
        "ui:widget": "customInputWidget"
      }
    },
    "Medication Provided": {
      classNames: "header",
      "ui:options":  {
        addable: true,
        removable: true,
        orderable: false,
        addButtonText: "Add Medication",
        removeButtonText: "Remove Medication"
      },
      items: {
        Code: {
          "ui:widget": "customInputWidget"
        },
        System: {
          "ui:widget": "customInputWidget"
        },
        Display: {
          "ui:widget": "customInputWidget"
        },
        Dosage: {
          Value: {
            "ui:widget": "customInputWidget"
          },
          Unit: {
            Code: {
              "ui:widget": "customInputWidget"
            },
            System: {
              "ui:widget": "customInputWidget"
            },
            Display: {
              "ui:widget": "customInputWidget"
            }
          }
        },
        Date: {
          "ui:widget": "customInputWidget"
        },
        Frequency: {
          "ui:widget": "customInputWidget"
        }
      }
    },
    Death_Date: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Date_Discharged: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Trigger_Code: {
      classNames: "header",
      "ui:options":  {
        addable: true,
        removable: true,
        orderable: false,
        addButtonText: "Add Trigger",
        removeButtonText: "Remove Trigger"
      },
      items: {
        Code: {
          "ui:widget": "customInputWidget"
        },
        System: {
          "ui:widget": "customInputWidget"
        },
        Display: {
          "ui:widget": "customInputWidget"
        }
      }
    },
    Lab_Tests_Performed: {
      classNames: "header",
      "ui:options":  {
        addable: true,
        removable: true,
        orderable: false,
        addButtonText: "Add Lab Test",
        removeButtonText: "Remove Lab Test"
      },
      items: {
        Value: {
          "ui:widget": "customInputWidget"
        },
        ResultStatus: {

        }
      }
    }
  },
  Facility: {
    "ui:field": "grid",
    "ui:options": {
      "order": [
        {
          ID: { md: 3 },
          Name: { md: 3 },
          Phone: { md: 3 },
          Address: { md: 3 }
        },
        {
          Fax: { md: 3 },
          Hospital_Unit: { md: 3 }
        }
      ]
    },
    ID: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Name: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Phone: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Address: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Fax: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    },
    Hospital_Unit: {
      classNames: "header",
      "ui:widget": "customInputWidget"
    }
  }

}

export { uiSchema };
