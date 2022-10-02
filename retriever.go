package twitchvhr

import (
	"fmt"
	"regexp"
)

var (
	hostRe = regexp.MustCompile(`https://([a-z0-9-]+\.[a-z]+\.[a-z]{2,3})\/`)
)

func RetrieveFeeds(vodID string) []Hosts {
	hosts := make([]Hosts, 0)
	feedsResponse := FetchFeeds(vodID)
	for _, f := range feedsResponse {
		feed := hostRe.FindStringSubmatch(f)
		if feed == nil {
			fmt.Errorf("Feed URL %v does not contain a valid host.", f)
		} else if UniqueHost(hosts, feed[1]) {
			hosts = append(hosts, Hosts{Name: feed[1]})
		}
	}
	return hosts
}

func UniqueHost(hosts []Hosts, host string) bool {
	for i := range hosts {
		if hosts[i].Name == host {
			return false
		}
	}
	return true
}
