package twitchvhr

import (
	"net/http"
	"regexp"
)

var (
	hostRe = regexp.MustCompile(`https://([a-z0-9-]+\.[a-z]+\.[a-z]{2,3})\/`)
)

func RetrieveFeeds(vodID string, client http.Client, feedsOptions FeedsOption) []string {
	hosts := make([]string, 0)
	feedsResponse := FetchFeeds(vodID, client, feedsOptions)
	for _, f := range feedsResponse {
		feed := hostRe.FindStringSubmatch(f)
		if feed != nil && UniqueHost(hosts, feed[1]) {
			hosts = append(hosts, feed[1])
		}
	}
	return hosts
}

func UniqueHost(hosts []string, host string) bool {
	for i := range hosts {
		if hosts[i] == host {
			return false
		}
	}
	return true
}
