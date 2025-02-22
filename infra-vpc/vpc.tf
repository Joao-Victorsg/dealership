data "aws_availability_zones" "available_zones" {
  state = "available"
}

resource "aws_vpc" "vpc_dealership" {
  enable_dns_support = true
  enable_dns_hostnames = true
  cidr_block = "10.32.0.0/16"
  tags = {
    name = "vpc_dealership"
  }
}

resource "aws_subnet" "public" {
  count                   = 3
  cidr_block              = cidrsubnet(aws_vpc.vpc_dealership.cidr_block, 8, 3 + count.index)
  availability_zone       = data.aws_availability_zones.available_zones.names[count.index]
  vpc_id                  = aws_vpc.vpc_dealership.id
  map_public_ip_on_launch = true
}

resource "aws_subnet" "private" {
  count             = 3
  cidr_block        = cidrsubnet(aws_vpc.vpc_dealership.cidr_block, 8, count.index)
  availability_zone = data.aws_availability_zones.available_zones.names[count.index]
  vpc_id            = aws_vpc.vpc_dealership.id
}

resource "aws_internet_gateway" "gateway" {
  vpc_id = aws_vpc.vpc_dealership.id
}

resource "aws_route" "internet_access" {
  route_table_id         = aws_vpc.vpc_dealership.main_route_table_id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.gateway.id
}

resource "aws_eip" "gateway" {
  count      = 3
  depends_on = [aws_internet_gateway.gateway]
}

resource "aws_nat_gateway" "gateway" {
  count         = 3
  subnet_id     = element(aws_subnet.public.*.id, count.index)
  allocation_id = element(aws_eip.gateway.*.id, count.index)
}

resource "aws_route_table" "private" {
  count  = 3
  vpc_id = aws_vpc.vpc_dealership.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = element(aws_nat_gateway.gateway.*.id, count.index)
  }
}

resource "aws_route_table_association" "private" {
  count          = 3
  subnet_id      = element(aws_subnet.private.*.id, count.index)
  route_table_id = element(aws_route_table.private.*.id, count.index)
}
