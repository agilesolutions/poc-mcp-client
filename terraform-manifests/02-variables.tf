# Input Variables
# 1. Business Unit Name
variable "business_unit" {
  description = "Business Unit Name"
  type        = string
  default     = "agilesolutions"
}
# 2. Environment Name
variable "environment" {
  description = "Environment Name"
  type        = string
  default     = "dev"
}
# 3. Resource Group Name
variable "resoure_group_name" {
  description = "Resource Group Name"
  type        = string
  default     = "ai"
}
# 4. Resource Group Location
variable "resoure_group_location" {
  description = "Resource Group Location"
  type        = string
  default     = "eastus"
}

variable "sku" {
  description = "The pricing tier of the search service you want to create (for example, basic or standard)."
  default     = "standard"
  type        = string
  validation {
    condition     = contains(["free", "basic", "standard", "standard2", "standard3", "storage_optimized_l1", "storage_optimized_l2"], var.sku)
    error_message = "The sku must be one of the following values: free, basic, standard, standard2, standard3, storage_optimized_l1, storage_optimized_l2."
  }
}

variable "replica_count" {
  type        = number
  description = "Replicas distribute search workloads across the service. You need at least two replicas to support high availability of query workloads (not applicable to the free tier)."
  default     = 1
  validation {
    condition     = var.replica_count >= 1 && var.replica_count <= 12
    error_message = "The replica_count must be between 1 and 12."
  }
}

variable "partition_count" {
  type        = number
  description = "Partitions allow for scaling of document count as well as faster indexing by sharding your index over multiple search units."
  default     = 1
  validation {
    condition     = contains([1, 2, 3, 4, 6, 12], var.partition_count)
    error_message = "The partition_count must be one of the following values: 1, 2, 3, 4, 6, 12."
  }
}