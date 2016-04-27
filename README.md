# Syntax-Normalizer-Filters

---
## Introduction

The Syntax Normalizer component is responsible for converting all data structures that are necessary for the transformation into a standard form. The reason for converting them, is because data transformation components can deal with a limited set of standard data structures. In many cases the local syntax rules of the provider can be so complicated, or even non-deterministic, that it is often more effective to use a set of custom filtering rules, that resolve one structural feature at a time, and verify it with the source schema experts. 

Starting from the Provider Institution, the Syntax Normalizer can be used to normalize the provider's records. It exploits local syntax rules and produces a new provider schema definition, called Effective Provider Schema. Normalization is quite often needed in date fields or in fields that contain concatenated information. 

## The Filters

Three different syntax normalization filters have been implemented. The functionality and the design of the filters were based on actual requirements coming from the users of the X3ML framework.

The first filter normalizes the syntax of an XML file by rearranging its structure in order to fix syntax issues, so that it becomes compliant with the concepts of a semantic model. The second filter assists the users inspecting the values of specific elements in an XM file and update specific values while preserving the original ones, performing a type of terminology mapping. The third filter's main functionality is to split the values of an element, to create sub-elements and assign these values to them.

The filters were implemented as maven java projects and can be executed either by completing a properties file and running the jars, or by the command line. More information on the filters (implementation, user manual etc) are in the corresponding pdf file of each filters project

## Contacts
### Main Contacts
*  Martin Doerr &lt;martin@ics.forth.gr&gt;
*  Chryssoula Bekiari &lt;bekiari@ics.forth.gr&gt;

### Main Developers
*  Nikos Minadakis &lt;minadakn@ics.forth.gr&gt;
